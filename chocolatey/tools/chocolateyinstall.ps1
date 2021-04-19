$version = '2.4.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '6E8B737868895200127B8C2C1F45B7DC0F705E1D940A8E2C72E0719ACC182436'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
