$version = '2.1.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '841738D67DACE6618082486AB85D8814DF4D13B3A88D4A514DF4000E90AC8109'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
