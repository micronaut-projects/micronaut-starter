$version = '2.5.9'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C403B8642BBABF669CB80A032AD7A17C5169E36C5486A65652641267956D4A73'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
