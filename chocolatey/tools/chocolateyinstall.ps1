$version = '4.10.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'ABA839138B0B99EC5ED580842B94E13EAA38B4C249FBC2AD5091754DF58ADC5A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
