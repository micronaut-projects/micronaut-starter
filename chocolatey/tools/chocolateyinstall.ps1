$version = '4.7.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '121C6674DB359E83D003DF35E4CEF4C28739F1DEFC156D29B69BC128452E873D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
