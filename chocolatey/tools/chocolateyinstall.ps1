$version = '4.10.11'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '5AC31730B345F0AC3CB064C961DE833A2FAC46C897BDEBE88444E4666DF40123'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
